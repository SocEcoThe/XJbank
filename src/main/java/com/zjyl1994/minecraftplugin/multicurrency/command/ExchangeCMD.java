/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zjyl1994.minecraftplugin.multicurrency.command;

import com.zjyl1994.minecraftplugin.multicurrency.MultiCurrencyPlugin;
import static com.zjyl1994.minecraftplugin.multicurrency.services.CurrencyService.isCurrencyOwner;
import com.zjyl1994.minecraftplugin.multicurrency.services.ExchangeService;
import com.zjyl1994.minecraftplugin.multicurrency.utils.OperateResult;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author zjyl1994
 */
public class ExchangeCMD {

    private ExchangeCMD() {
    }

    private static class SingletonHolder {

        private static final ExchangeCMD INSTANCE = new ExchangeCMD();
    }

    public static ExchangeCMD getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getCommand(Player p, String currencyCodeFrom, String currencyCodeTo) {
        Bukkit.getScheduler().runTaskAsynchronously(MultiCurrencyPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                OperateResult getResult = ExchangeService.getExchangeRate(currencyCodeFrom.toUpperCase(), currencyCodeTo.toUpperCase());
                Bukkit.getScheduler().runTask(MultiCurrencyPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (getResult.getSuccess()) {
                            BigDecimal exchangeRate = (BigDecimal) getResult.getData();
                            if(exchangeRate.compareTo(BigDecimal.ZERO)==0){
                                p.sendMessage("货币发行人未指定二者汇率");
                            }else{
                                p.sendMessage("1" + currencyCodeFrom.toUpperCase() + " 可兑换 " + exchangeRate.setScale(4, RoundingMode.DOWN) + currencyCodeTo.toUpperCase());
                            }
                        } else {
                            p.sendMessage(getResult.getReason());
                        }
                    }
                });
            }
        });
    }

    public void setCommand(Player p, String currencyCodeFrom, String currencyCodeTo, String rate) {
        BigDecimal exchangeRate = new BigDecimal(rate).setScale(4, RoundingMode.DOWN);
        Bukkit.getScheduler().runTaskAsynchronously(MultiCurrencyPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                String message;
                if (isCurrencyOwner(currencyCodeTo, p.getName())){
                    OperateResult getResult = ExchangeService.setExchangeRate(currencyCodeFrom.toUpperCase(), currencyCodeTo.toUpperCase(), exchangeRate);
                 if (getResult.getSuccess()) {
                            message = "汇率已设置，1" + currencyCodeFrom.toUpperCase() + " -> " + exchangeRate + currencyCodeTo.toUpperCase();
                        } else {
                            message = getResult.getReason();
                        }
                }else{
                    message = "您并非此货币的发行者";
                }
                Bukkit.getScheduler().runTask(MultiCurrencyPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        p.sendMessage(message);
                    }
                });
            }
        });
    }

    public void exchangeCommand(Player p, String currencyCodeFrom, String currencyCodeTo, String amount) {
        BigDecimal exchangeAmount = new BigDecimal(amount).setScale(4, RoundingMode.DOWN);
        Bukkit.getScheduler().runTaskAsynchronously(MultiCurrencyPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                OperateResult runResult = ExchangeService.exchange(p.getName(),currencyCodeFrom.toUpperCase(), currencyCodeTo.toUpperCase(),exchangeAmount);
                Bukkit.getScheduler().runTask(MultiCurrencyPlugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if (runResult.getSuccess()) {
                            p.sendMessage("货币兑换指示已成功完成");
                        } else {
                            p.sendMessage(runResult.getReason());
                        }
                    }
                });
            }
        });
    }
}
