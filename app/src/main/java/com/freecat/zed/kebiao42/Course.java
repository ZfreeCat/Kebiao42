package com.freecat.zed.kebiao42;

/**
 * Created by Mr.Z on 2016/3/22.
 */
public class Course {
    /**
     * c_l 4 course location
     * c_t 4 course teacher
     * c_n 4 course name
     * c_p 4 course period(course length)
     * c_p_s 4 course period start
     * c_w 4 course weekday
     */
    String c_l,c_t,c_n;
    Integer c_p_s,c_p,c_w;

    public String getC_l() {
        return c_l;
    }

    public void setC_l(String c_l) {
        this.c_l = c_l;
    }

    public String getC_t() {
        return c_t;
    }

    public void setC_t(String c_t) {
        this.c_t = c_t;
    }

    public String getC_n() {
        return c_n;
    }

    public void setC_n(String c_n) {
        this.c_n = c_n;
    }

    public Integer getC_p_s() {
        return c_p_s;
    }

    public void setC_p_s(Integer c_p_s) {
        this.c_p_s = c_p_s;
    }

    public Integer getC_p() {
        return c_p;
    }

    public void setC_p(Integer c_p) {
        this.c_p = c_p;
    }

    public Integer getC_w() {
        return c_w;
    }

    public void setC_w(Integer c_w) {
        this.c_w = c_w;
    }
}
