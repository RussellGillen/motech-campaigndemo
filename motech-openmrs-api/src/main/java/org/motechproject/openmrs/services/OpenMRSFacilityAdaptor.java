package org.motechproject.openmrs.services;

import org.apache.commons.lang.StringUtils;
import org.motechproject.mrs.model.Facility;
import org.motechproject.mrs.services.MRSFacilityAdaptor;
import org.openmrs.Location;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OpenMRSFacilityAdaptor implements MRSFacilityAdaptor {
    @Autowired
    private org.openmrs.api.LocationService locationService;

    @Override
    public Facility saveFacility(Facility facility) {
        String facilityId = facility.getId();
        Location location = new Location();
        if(facilityId != null) {
            location = locationService.getLocation(Integer.parseInt(facilityId));
        }

        location.setName(facility.getName());
        location.setCountry(facility.getCountry());
        location.setAddress6(facility.getRegion());
        location.setStateProvince(facility.getStateProvince());
        location.setCountyDistrict(facility.getCountyDistrict());

        Location savedLocation = this.locationService.saveLocation(location);
        return convertLocationToFacility(savedLocation);
    }

    @Override
    public List<Facility> getFacilities() {
        List<Location> locations = locationService.getAllLocations();
        List<Facility> facilities = new ArrayList<Facility>();
        for (Location location : locations) {
            facilities.add(convertLocationToFacility(location));
        }
        return facilities;
    }

    @Override
    public List<Facility> getFacilities(String locationName) {
        final List<Location> locations = locationService.getLocations(locationName);
        final ArrayList<Facility> facilities = new ArrayList<Facility>();
        for (Location location : locations) {
            facilities.add(convertLocationToFacility(location));
        }
        return facilities;
    }

    @Override
    public Facility getFacility(String facilityId) {
        if(StringUtils.isEmpty(facilityId)) return null;
        final Location location = getLocation(facilityId);
        return (location != null) ? convertLocationToFacility(location) : null;
    }

    Location getLocation(String facilityId) {
        return locationService.getLocation(Integer.parseInt(facilityId));
    }

    Facility convertLocationToFacility(Location savedLocation) {
        return new Facility(String.valueOf(savedLocation.getId()), savedLocation.getName(), savedLocation.getCountry(),
                savedLocation.getAddress6(), savedLocation.getCountyDistrict(), savedLocation.getStateProvince());
    }
}
