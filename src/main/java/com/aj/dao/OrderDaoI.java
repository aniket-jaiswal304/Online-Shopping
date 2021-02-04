package com.aj.dao;

import com.aj.model.CartInfo;
import com.aj.model.OrderDetailInfo;
import com.aj.model.OrderInfo;
import com.aj.model.PaginationResult;

import java.util.List;

public interface OrderDaoI
{
    void saveOrder(CartInfo cartInfo);
    public PaginationResult<OrderInfo> listOrderInfo(int page,
                                                     int maxResult, int maxNavigationPage);
    OrderInfo getOrderInfo(String orderId);
    List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}
