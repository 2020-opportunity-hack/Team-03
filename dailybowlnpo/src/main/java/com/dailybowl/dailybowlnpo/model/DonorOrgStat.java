package com.dailybowl.dailybowlnpo.model;

public class DonorOrgStat {
    private String date;
    private int weight;
    private int valuation;

    public DonorOrgStat(String date, int weight, int valuation) {
        this.date = date;
        this.weight = weight;
        this.valuation = valuation;
    }

    public DonorOrgStat() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getValuation() {
        return valuation;
    }

    public void setValuation(int valuation) {
        this.valuation = valuation;
    }

    @Override
    public String toString() {
        return "DonorOrgStat{" +
                "date='" + date + '\'' +
                ", weight=" + weight +
                ", valuation=" + valuation +
                '}';
    }
}
