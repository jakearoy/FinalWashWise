package com.example.afinal;

import android.os.Parcel;
import android.os.Parcelable;

public class Laundromat implements Parcelable {

     public static final Creator<Laundromat> CREATOR = new Creator<Laundromat>() {
          @Override
          public Laundromat createFromParcel(Parcel in) {
               return new Laundromat(in);
          }

          @Override
          public Laundromat[] newArray(int size) {
               return new Laundromat[size];
          }
     };

     private Laundromat(Parcel in) {
          name = in.readString();
          address = in.readString();
          washPrice = in.readDouble();
          ironPrice = in.readDouble();
          dryCleanPrice = in.readDouble();
          number = in.readString();
          washIronPrice = in.readDouble();
          distance = in.readDouble();
     }


     String name;
     String address;
     double washPrice;
     double ironPrice;
     double dryCleanPrice;
     String number;
     double washIronPrice;
     double distance;

     public Laundromat() {}

     public Laundromat(String name, String address, double washPrice, double ironPrice, double dryCleanPrice, String number, double washIronPrice) {
          this.name = name;
          this.address = address;
          this.washPrice = washPrice;
          this.ironPrice = ironPrice;
          this.dryCleanPrice = dryCleanPrice;
          this.number = number;
          this.washIronPrice = washIronPrice;
     }

     public String getName() {
          return name;
     }

     public String getAddress() {
          return address;
     }

     public double getWashPrice() {
          return washPrice;
     }

     public double getIronPrice() {
          return ironPrice;
     }

     public double getDryCleanPrice() {
          return dryCleanPrice;
     }

     public String getNumber() {
          return number;
     }

     public double getWashIronPrice() {
          return washIronPrice;
     }

     public double getDistance() {
          return distance;
     }

     public void setDistance(double distance) {
          this.distance = distance;
     }

     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(name);
          dest.writeString(address);
          dest.writeDouble(washPrice);
          dest.writeDouble(ironPrice);
          dest.writeDouble(dryCleanPrice);
          dest.writeString(number);
          dest.writeDouble(washIronPrice);
          dest.writeDouble(distance);
     }
}
