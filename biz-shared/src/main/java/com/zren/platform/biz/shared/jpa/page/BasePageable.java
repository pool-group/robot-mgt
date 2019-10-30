package com.zren.platform.biz.shared.jpa.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 自定义PageRequest
 *
 * @author k.y
 * @version Id: BasePageable.java, v 0.1 2018年11月07日 下午17:11 k.y Exp $
 */
public class BasePageable extends PageRequest {

    public BasePageable(int size, Sort sort) {
        super(0,size,sort);
    }

}
