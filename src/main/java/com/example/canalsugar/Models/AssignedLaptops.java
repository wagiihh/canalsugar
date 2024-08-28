package com.example.canalsugar.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "AssignedLaptops")
public class AssignedLaptops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alID;

     @NotNull
    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", insertable = true, updatable = true)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "LaptopID", referencedColumnName = "LaptopID", insertable = true, updatable = true)
    private Laptop laptop;

    public AssignedLaptops() {
    }

    public AssignedLaptops(int alID, User user, Laptop laptop) {
        this.alID = alID;
        this.user = user;
        this.laptop = laptop;
    }

    public int getAlID() {
        return this.alID;
    }

    public void setAlID(int alID) {
        this.alID = alID;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Laptop getLaptop() {
        return this.laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public AssignedLaptops alID(int alID) {
        setAlID(alID);
        return this;
    }

    public AssignedLaptops user(User user) {
        setUser(user);
        return this;
    }

    public AssignedLaptops laptop(Laptop laptop) {
        setLaptop(laptop);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AssignedLaptops)) {
            return false;
        }
        AssignedLaptops assignedLaptops = (AssignedLaptops) o;
        return alID == assignedLaptops.alID && Objects.equals(user, assignedLaptops.user) && Objects.equals(laptop, assignedLaptops.laptop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alID, user, laptop);
    }

    @Override
    public String toString() {
        return "{" +
            " alID='" + getAlID() + "'" +
            ", user='" + getUser() + "'" +
            ", laptop='" + getLaptop() + "'" +
            "}";
    }


}
