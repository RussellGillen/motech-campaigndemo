package org.motechproject.scheduletracking.api.domain;

import org.motechproject.valueobjects.WallTime;

public class AlertConfiguration {
    private WallTime startOffset;
    private WallTime interval;
    private int totalCount;

    public AlertConfiguration(WallTime startOffset, WallTime interval, int totalCount) {
        this.startOffset = startOffset;
        this.interval = interval;
        this.totalCount = totalCount;
    }
}
