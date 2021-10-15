package com.gfbusinessschool.bean;

import java.util.List;

public class ProvinceCityBean {

    /**
     * ssqid : 130000
     * ssqname : 河北省
     * ssqename :
     * cities : {"city":[]}
     */

    private String ssqid;
    private String ssqname;
    private List<CitiesBean> city;

    public String getSsqid() {
        return ssqid;
    }

    public void setSsqid(String ssqid) {
        this.ssqid = ssqid;
    }

    public String getSsqname() {
        return ssqname;
    }

    public void setSsqname(String ssqname) {
        this.ssqname = ssqname;
    }

    public List<CitiesBean> getCity() {
        return city;
    }

    public void setCity(List<CitiesBean> city) {
        this.city = city;
    }

    public static class CitiesBean {

        /**
         * ssqid : 110100
         * ssqname : 市辖区
         * ssqename :
         * areas : {"area":[{"ssqid":"110101","ssqname":"东城区","ssqename":""},{"ssqid":"110102","ssqname":"西城区","ssqename":""},{"ssqid":"110103","ssqname":"崇文区","ssqename":""},{"ssqid":"110104","ssqname":"宣武区","ssqename":""},{"ssqid":"110105","ssqname":"朝阳区","ssqename":""},{"ssqid":"110106","ssqname":"丰台区","ssqename":""},{"ssqid":"110107","ssqname":"石景山区","ssqename":""},{"ssqid":"110108","ssqname":"海淀区","ssqename":""},{"ssqid":"110109","ssqname":"门头沟区","ssqename":""},{"ssqid":"110111","ssqname":"房山区","ssqename":""},{"ssqid":"110112","ssqname":"通州区","ssqename":""},{"ssqid":"110113","ssqname":"顺义区","ssqename":""},{"ssqid":"110114","ssqname":"昌平区","ssqename":""},{"ssqid":"110115","ssqname":"大兴区","ssqename":""},{"ssqid":"110116","ssqname":"平谷区","ssqename":""},{"ssqid":"110117","ssqname":"怀柔区","ssqename":""}]}
         */

        private String ssqid;
        private String ssqname;
        private AreasBean areas;

        public String getSsqid() {
            return ssqid;
        }

        public void setSsqid(String ssqid) {
            this.ssqid = ssqid;
        }

        public String getSsqname() {
            return ssqname;
        }

        public void setSsqname(String ssqname) {
            this.ssqname = ssqname;
        }

        public AreasBean getAreas() {
            return areas;
        }

        public void setAreas(AreasBean areas) {
            this.areas = areas;
        }

        public static class AreasBean {
            private List<AreaBean> area;

            public List<AreaBean> getArea() {
                return area;
            }

            public void setArea(List<AreaBean> area) {
                this.area = area;
            }

            public static class AreaBean {
                /**
                 * ssqid : 110101
                 * ssqname : 东城区
                 * ssqename :
                 */

                private String ssqid;
                private String ssqname;
                private String ssqename;

                public String getSsqid() {
                    return ssqid;
                }

                public void setSsqid(String ssqid) {
                    this.ssqid = ssqid;
                }

                public String getSsqname() {
                    return ssqname;
                }

                public void setSsqname(String ssqname) {
                    this.ssqname = ssqname;
                }

                public String getSsqename() {
                    return ssqename;
                }

                public void setSsqename(String ssqename) {
                    this.ssqename = ssqename;
                }
            }
        }
    }
}