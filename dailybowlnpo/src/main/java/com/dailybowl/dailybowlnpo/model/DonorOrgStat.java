package com.dailybowl.dailybowlnpo.model;

public class DonorOrgStat {
    private String date;
    private int weight;
    private int valuation;
    private String name;
    private String foodType;
    private String deliveredTo;


    public DonorOrgStat() {
    }

    public DonorOrgStat(String date, int weight, int valuation, String name, String foodType, String deliveredTo) {
        this.date = date;
        this.weight = weight;
        this.valuation = valuation;
        this.name = name;
        this.foodType = foodType;
        this.deliveredTo = deliveredTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDeliveredTo() {
        return deliveredTo;
    }

    public void setDeliveredTo(String deliveredTo) {
        this.deliveredTo = deliveredTo;
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
                ", name='" + name + '\'' +
                ", foodType='" + foodType + '\'' +
                ", deliveredTo='" + deliveredTo + '\'' +
                '}';
    }
}
