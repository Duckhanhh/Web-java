package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.BookDTO;
import org.bpm.abcbook.dto.request.UpdateBookRequest;
import org.bpm.abcbook.model.Books;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BooksMapper {
    Books UpdateBookRequestToBooks(UpdateBookRequest updateBookRequest);

    BookDTO booksToBookDTO(Books books);

    List<BookDTO> booksToBookDTOs(List<Books> booksList);
}
