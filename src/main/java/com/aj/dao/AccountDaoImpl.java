package com.aj.dao;

import com.aj.entity.Account;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AccountDaoImpl implements AccountDaoI
{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Account findAccount(String username)
    {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Account.class);
        criteria.add(Restrictions.eq("username", username));
        return (Account)criteria.uniqueResult();
    }
}
