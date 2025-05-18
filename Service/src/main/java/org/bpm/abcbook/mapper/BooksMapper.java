package org.bpm.abcbook.mapper;

import org.bpm.abcbook.dto.request.UpdateBookRequest;
import org.bpm.abcbook.model.Books;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BooksMapper {
    Books UpdateBookRequestToBooks(UpdateBookRequest updateBookRequest);
}
