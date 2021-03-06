package fi.metatavu.edelphi.dao.orders;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.edelphi.dao.GenericDAO;
import fi.metatavu.edelphi.domainmodel.orders.OrderHistory;
import fi.metatavu.edelphi.domainmodel.orders.OrderStatus;
import fi.metatavu.edelphi.domainmodel.orders.Plan;
import fi.metatavu.edelphi.domainmodel.users.SubscriptionLevel;
import fi.metatavu.edelphi.domainmodel.users.User;

@ApplicationScoped
public class OrderHistoryDAO extends GenericDAO<OrderHistory> {

  @SuppressWarnings ("squid:S00107")
  public OrderHistory create(Date created, User user, Plan plan, SubscriptionLevel subscriptionLevel, OrderStatus status, String currency, Double price, String text, Integer days) {
    OrderHistory orderHistory = new OrderHistory();
    
    orderHistory.setCurrency(currency);
    orderHistory.setPrice(price);
    orderHistory.setText(text);
    orderHistory.setStatus(status);
    orderHistory.setDays(days);
    orderHistory.setSubscriptionLevel(subscriptionLevel);
    orderHistory.setUser(user);
    orderHistory.setPlan(plan);
    orderHistory.setCreated(created);
    
    return persist(orderHistory);
  }

  public OrderHistory updateStatus(OrderHistory orderHistory, OrderStatus status) {
    orderHistory.setStatus(status);
    return persist(orderHistory);
  }
  
  
}
