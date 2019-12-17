/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zjyl1994.minecraftplugin.multicurrency.services;

import com.zjyl1994.minecraftplugin.multicurrency.utils.AccountHelper;
import com.zjyl1994.minecraftplugin.multicurrency.utils.OperateResult;
import com.zjyl1994.minecraftplugin.multicurrency.utils.TxTypeEnum;
import java.math.BigDecimal;

/**
 * 支票逻辑
 *
 * @author zjyl1994
 */
public class CheckService {

    // 开出一张支票
    public static OperateResult makeCheck(String username, String currencyCode, BigDecimal amount) {
        return BankService.transferTo(username, AccountHelper.CHECK_CENTER, currencyCode, amount, TxTypeEnum.CHECK_TRANSFER_OUT, "支票开出");
    }

    // 兑现一张支票
    public static OperateResult cashCheck(String username, String currencyCode, BigDecimal amount) {
        return BankService.transferTo(AccountHelper.CHECK_CENTER, username, currencyCode, amount, TxTypeEnum.CHECK_TRANSFER_IN, "支票兑付");
    }
}
