package com.bkizilkaya.culturelbackend.utils;

import com.bkizilkaya.culturelbackend.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component

public class PaginationValidator {
    public void validatePageNumberIsExists(int pageNo, Page<?> page) {
        if (page.getTotalPages() < pageNo) {
            throw new ValidationException("Boyle bir sayfa bulunmamaktadir!");
        }
    }

}
