package com.curtesmalteser.jsoncurrencies.model;

import android.arch.lifecycle.ViewModel;

/**
 * Created by anton on 11/12/2017.
 */

public class ConverterModel extends ViewModel {

    private String base;
    private String baseCoin;
    private String baseDate;
    private double baseInput;
    private double baseRate;
    private double baseResult;

    // Reverse value model
    private String reverseBase;
    private String reverseCoin;
    private String reverseDate;
    private double reverseInput;
    private double reverseRate;
    private double reverseResult;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public double getBaseInput() {
        return baseInput;
    }

    public void setBaseInput(double baseInput) {
        this.baseInput = baseInput;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getBaseResult() {
        return baseResult;
    }

    public void setBaseResult(double baseResult) {
        this.baseResult = baseResult;
    }

    public String getReverseBase() {
        return reverseBase;
    }

    public void setReverseBase(String reverseBase) {
        this.reverseBase = reverseBase;
    }

    public String getReverseCoin() {
        return reverseCoin;
    }

    public void setReverseCoin(String reverseCoin) {
        this.reverseCoin = reverseCoin;
    }

    public String getReverseDate() {
        return reverseDate;
    }

    public void setReverseDate(String reverseDate) {
        this.reverseDate = reverseDate;
    }

    public double getReverseInput() {
        return reverseInput;
    }

    public void setReverseInput(double reverseInput) {
        this.reverseInput = reverseInput;
    }

    public double getReverseRate() {
        return reverseRate;
    }

    public void setReverseRate(double reverseRate) {
        this.reverseRate = reverseRate;
    }

    public double getReverseResult() {
        return reverseResult;
    }

    public void setReverseResult(double reverseResult) {
        this.reverseResult = reverseResult;
    }
}
