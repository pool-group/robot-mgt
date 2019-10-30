package com.zren.platform.biz.shared.jpa.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class BasePageable extends PageRequest {

    public BasePageable(int start,int size, Sort sort) {
        super(start,size,sort);
    }

}
