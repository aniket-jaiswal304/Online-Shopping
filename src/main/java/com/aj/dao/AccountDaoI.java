package com.aj.dao;

import com.aj.entity.Account;

public interface AccountDaoI
{
    Account findAccount(String username);
}
