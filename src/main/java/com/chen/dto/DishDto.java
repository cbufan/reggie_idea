package com.chen.dto;

/**
 * description:
 * className:DishDto
 * author: chenqifan
 * date:2023/3/414:07
 **/

import com.chen.domain.Dish;
import com.chen.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
