package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.dto.CategoryDTO;
import org.bpm.abcbook.dto.RevenueDTO;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.OrderService;

import org.bpm.abcbook.util.DataUtil;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Named
@Data
@ViewScoped
public class DashBoardController {
    private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

    private LineChartModel lineModel;
    private List<RevenueDTO> listRevenue;
    private PieChartModel pieChartModel;
    private List<CategoryDTO> listCategory;
    private List<BookDTO> listBestSelling;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() {
        try {
            lineModel = new LineChartModel();
            pieChartModel = new PieChartModel();

            listRevenue = orderService.getRevenue();
            if (DataUtil.isNullOrEmpty(listRevenue)) {
                throw new AppException("RCC0000", "Lỗi khi lấy doanh thu");
            }

            listCategory = orderService.getFavoriteCategory();
            if (DataUtil.isNullOrEmpty(listCategory)) {
                throw new AppException("RCC0001", "Lỗi khi lấy danh sách thể loại được yêu thích");
            }

            listBestSelling = orderService.getBestSellingBooks();
            if (DataUtil.isNullOrEmpty(listBestSelling)) {
                throw new AppException("RCC0002", "Lỗi khi lấy danh sách bán chạy");
            }

            createLineChart();
            createPieChart();
        } catch (Exception e) {
            logger.error("Error initializing OrderController", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi hệ thống", ""));
        }
    }

    public void createLineChart() {
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = listRevenue.stream().map(RevenueDTO::getAmount).collect(Collectors.toUnmodifiableList());
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setLabel("Số tiền");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        List<String> labels = listRevenue.stream().map(revenue -> DataUtil.formatDateToString(revenue.getMonth())).toList();
        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Doanh số từng tháng");
        options.setTitle(title);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

    public void createPieChart() {
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = listCategory.stream().map(CategoryDTO::getNumberOfBook).collect(Collectors.toUnmodifiableList());
        dataSet.setData(values);
        data.addChartDataSet(dataSet);

        List<String> labels = listCategory.stream().map(CategoryDTO::getName).toList();
        data.setLabels(labels);

        PieChartOptions options = new PieChartOptions();
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Top các thể loại được yêu thích");
        title.setFontSize(15);
        options.setTitle(title);

        pieChartModel.setOptions(options);
        pieChartModel.setData(data);
    }
}
