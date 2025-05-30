package com.chensoul.security.spring.userlocation;

import com.chensoul.security.spring.user.User;
import com.chensoul.security.spring.user.UserRepository;
import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.UUID;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Service
@RequiredArgsConstructor
public class UserLocationService {
    private final UserLocationRepository userLocationRepository;
    private final NewLocationTokenRepository newLocationTokenRepository;
    private final UserRepository userRepository;
    private final Environment environment;
    private final DatabaseReader databaseReader;

    public UserLocation findByCountryAndUser(String country, User user) {
        return userLocationRepository.findByCountryAndUser(country, user);
    }

    public UserLocation save(UserLocation userLocation) {
        return userLocationRepository.save(userLocation);
    }

    public NewLocationToken isNewLoginLocation(String username, String ip) {
        if (!isGeoIpLibEnabled()) {
            return null;
        }

        try {
            final String country = getCountryByIp(ip);
            final User user = userRepository.findByName(username);
            final UserLocation loc = userLocationRepository.findByCountryAndUser(country, user);
            if ((loc == null) || !loc.isEnabled()) {
                return createNewLocationToken(country, user);
            }
        } catch (final Exception e) {
            return null;
        }
        return null;
    }

    private String getCountryByIp(String ip) {
        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            return databaseReader.country(ipAddress)
                    .getCountry()
                    .getName();
        } catch (final Exception e) {
            return "局域网";
        }
    }

    public void addUserLocation(User user, String ip) {
        if (!isGeoIpLibEnabled()) {
            return;
        }

        try {
            final InetAddress ipAddress = InetAddress.getByName(ip);
            final String country = databaseReader.country(ipAddress)
                    .getCountry()
                    .getName();
            UserLocation loc = new UserLocation(country, user);
            loc.setEnabled(true);
            userLocationRepository.save(loc);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String checkNewLocationToken(String token) {
        final NewLocationToken locToken = newLocationTokenRepository.findByToken(token);
        if (locToken == null) {
            return null;
        }
        UserLocation userLoc = locToken.getUserLocation();
        userLoc.setEnabled(true);
        userLoc = userLocationRepository.save(userLoc);
        newLocationTokenRepository.delete(locToken);
        return userLoc.getCountry();
    }

    private boolean isGeoIpLibEnabled() {
        return Boolean.parseBoolean(environment.getProperty("geo.ip.enabled", "true"));
    }

    private NewLocationToken createNewLocationToken(String country, User user) {
        UserLocation loc = new UserLocation(country, user);
        loc = userLocationRepository.save(loc);

        final NewLocationToken token = new NewLocationToken(UUID.randomUUID()
                .toString(), loc);
        return newLocationTokenRepository.save(token);
    }
}
