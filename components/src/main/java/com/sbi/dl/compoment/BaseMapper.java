package com.sbi.dl.compoment;

import java.util.List;

public interface BaseMapper<D, E> {

    /**
     * Model2DTO
     *
     * @param model /
     * @return /
     */
    D toDTO(E model);

    /**
     * Model List 2 DTO List
     *
     * @param modelList /
     * @return /
     */
    List<D> toDTO(List<E> modelList);
}
