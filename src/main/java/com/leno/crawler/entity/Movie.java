package com.leno.crawler.entity;

import javax.persistence.*;

/**
 * 描述:
 * 电影类
 *
 * @author Leo
 * @create 2017-12-26 下午 6:52
 */
@Entity(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MYSQL时可以这样使用自增
    //@Column(name = "movieId")
    private Long movieId;
    /**
     * 电影名
     */
    @Column(name = "name")
    private String name;
    /**
     * 导演
     */
    @Column(name = "director")
    private String director;
    /**
     * 编剧
     */
    @Column(name = "scenarist")
    private String scenarist;
    /**
     * 主演
     */
    @Column(name = "actors")
    private String actors;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;
    /**
     * 制片国家/地区
     */
    @Column(name = "country")
    private String country;
    /**
     * 语言
     */
    @Column(name = "language")
    private String language;
    /**
     * 上映日期
     */
    @Column(name = "releaseDate")
    private String releaseDate;
    /**
     * 片场
     */
    @Column(name = "runtime")
    private String runtime;
    /**
     * 豆瓣评分
     */
    @Column(name = "ratingNum")
    private String ratingNum;
    /**
     * 标签
     */
    @Column(name = "tags")
    private String tags;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getScenarist() {
        return scenarist;
    }

    public void setScenarist(String scenarist) {
        this.scenarist = scenarist;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        runtime = runtime;
    }

    public String getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(String ratingNum) {
        this.ratingNum = ratingNum;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", language='" + language + '\'' +
                ", ratingNum='" + ratingNum + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        return getName().equals(movie.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
