package com.TravelApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TravelApp.dto.GeolocationRequest;
import com.TravelApp.entity.User;
import com.TravelApp.repository.UserRepository;

@Service
public class GeolocationService {

    @Autowired
    private UserRepository userRepository;

    public double calculateDistance(GeolocationRequest geolocationRequest, double latitude, double longtitude) {
        double earthRadius = 6371.0;

        double radRequestLat = Math.toRadians(geolocationRequest.getLatitude());
        double radRequestLong = Math.toRadians(geolocationRequest.getLongtitude());
        double radLat = Math.toRadians(latitude);
        double radLon = Math.toRadians(longtitude);

        double deltaLat = radLat - radRequestLat;
        double deltaLon = radLon - radRequestLong;
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                   Math.cos(radRequestLat) * Math.cos(radLat) * Math.pow(Math.sin(deltaLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public User setUserLocation(User user, GeolocationRequest geolocationRequest){
        user.setLatitude(geolocationRequest.getLatitude());
        user.setLongtitude(geolocationRequest.getLongtitude());
        return userRepository.save(user);
    }
}
