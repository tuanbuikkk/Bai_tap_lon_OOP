package com.yourteam.englishapp.data.local.entity;

import com.yourteam.englishapp.model.AccessLevel;
import com.yourteam.englishapp.model.SubscriptionStatus;

/** POJO ánh xạ bảng Subscriptions. Thời gian lưu dạng epoch millis. */
public class Subscription {
    private long id;
    private long userId;
    private String planName;            // "FREE", "PREMIUM_MONTH"...
    private AccessLevel accessLevel;
    private SubscriptionStatus status;
    private long startDate;
    private long endDate;

    public Subscription() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public AccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(AccessLevel accessLevel) { this.accessLevel = accessLevel; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }
}
