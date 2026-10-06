package com.smartcampus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CampusLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String crowdStatus;
    private boolean blocked;

    private int totalVotes;
    private int crowdedVotes;
    private int blockedVotes;

    public CampusLocation() {
    }

    public CampusLocation(String name, String crowdStatus, boolean blocked) {
        this.name = name;
        this.crowdStatus = crowdStatus;
        this.blocked = blocked;
        this.totalVotes = 0;
        this.crowdedVotes = 0;
        this.blockedVotes = 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCrowdStatus() {
        return crowdStatus;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public int getCrowdedVotes() {
        return crowdedVotes;
    }

    public int getBlockedVotes() {
        return blockedVotes;
    }

    public double getCrowdedPercentage() {
        if (totalVotes == 0) {
            return 0;
        }

        return (crowdedVotes * 100.0) / totalVotes;
    }

    public double getBlockedPercentage() {
        if (totalVotes == 0) {
            return 0;
        }

        return (blockedVotes * 100.0) / totalVotes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCrowdStatus(String crowdStatus) {
        this.crowdStatus = crowdStatus;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public void setCrowdedVotes(int crowdedVotes) {
        this.crowdedVotes = crowdedVotes;
    }

    public void setBlockedVotes(int blockedVotes) {
        this.blockedVotes = blockedVotes;
    }
}