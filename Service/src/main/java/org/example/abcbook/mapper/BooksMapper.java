package org.example.abcbook.mapper;

import org.example.abcbook.dto.request.UpdateBookRequest;
import org.example.abcbook.model.Books;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BooksMapper {
    Books UpdateBookRequestToBooks(UpdateBookRequest updateBookRequest);
}
