package service;

import dao.LocationDao;
import dao.WeatherDao;
import model.Location;
import utils.mapper.LocationMapper;
import utils.mapper.WeatherMapper;
import model.WeatherDto;
import model.Weather;
import utils.averager.Averager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherService {

    final WeatherClient openWeatherMapClient;
    final WeatherClient weatherStackClient;
    final Averager<WeatherDto> weatherAverager;
    final WeatherMapper weatherMapper;
    final LocationMapper locationMapper;
    final LocationDao locationDao;
    final WeatherDao weatherDao;


    public WeatherService(WeatherClient openWeatherMapClient, WeatherClient weatherStackClient, Averager<WeatherDto> weatherAverager, WeatherMapper weatherMapper, LocationMapper locationMapper, LocationDao locationDao, WeatherDao weatherDao) {
        this.openWeatherMapClient = openWeatherMapClient;
        this.weatherStackClient = weatherStackClient;
        this.weatherAverager = weatherAverager;
        this.weatherMapper = weatherMapper;
        this.locationMapper = locationMapper;
        this.locationDao = locationDao;
        this.weatherDao = weatherDao;
    }

    public WeatherDto getAverageWeatherDtoByCity(String city) {
        WeatherDto weather1 = getWeatherDtoFromOpenWeatherMap(city);
        WeatherDto weather2 = getWeatherDtoFromWeatherStack(city);

        WeatherDto averageWeatherDto = getAverageWeatherDto(weather1, weather2);
        averageWeatherDto.setDate(LocalDate.now());

        saveWeather(averageWeatherDto);

        return averageWeatherDto;
    }

    public void saveWeather(WeatherDto weatherDto) {
        Weather weather = weatherMapper.toEntity(weatherDto);
        Location location = locationDao.findByCityAndCountry(weather.getLocation().getCityName(), weather.getLocation().getCountryName());
        if (location != null) {
            weather.setLocation(location);
            weatherDao.save(weather);
        } else {
            weatherDao.save(weather);
        }
    }

    public WeatherDto getWeatherDtoFromOpenWeatherMap(String city) {
        return openWeatherMapClient.getWeatherByCity(city);
    }


    public WeatherDto getWeatherDtoFromWeatherStack(String city) {
        return weatherStackClient.getWeatherByCity(city);
    }

    public WeatherDto getAverageWeatherDto(WeatherDto... weathers) {
        return weatherAverager.getAverage(weathers);
    }



    public void displayAllLocations() {
        List<WeatherDto> locations = getAllLocations().stream()
                .map(locationMapper::toDto)
                .toList();

        for (WeatherDto location : locations) {
            displayLocation(location);
        }
    }

    public List<Location> getAllLocations() {
        return locationDao.findAll();
    }

    public void displayWeather(WeatherDto weatherDto) {
        System.out.println("City: " + weatherDto.getCityName() +
                "\nCountry: " + weatherDto.getCountryName() +
                "\nRegion: " + weatherDto.getRegion() +
                "\nDate: " + weatherDto.getDate() +
                "\nTemperature: " + weatherDto.getTemperature() + " C" +
                "\nPressure: " + weatherDto.getPressure() + " Pa" +
                "\nHumidity: " + weatherDto.getHumidity() + " %" +
                "\nWind direction: " + weatherDto.getWindDirection() +
                "\nWind speed: " + weatherDto.getWindSpeed() + " km/hour");
    }

    public void displayLocation(WeatherDto weatherDto) {
        System.out.println("City: " + weatherDto.getCityName() + " | " +
                "Country: " + weatherDto.getCountryName() + " | " +
                "Region: " + weatherDto.getRegion() + " | " +
                "Coordinates: " + weatherDto.getLatitude() + ", " + weatherDto.getLongitude());
    }




}
