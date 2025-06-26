package com.xiaomi.cug.entity;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalData {
        private BigDecimal Mx;
        private BigDecimal Mi;
        private BigDecimal Ix;
        private BigDecimal Ii;

        public SignalData(BigDecimal mx, BigDecimal mi, String type) {
                if ("VOLTAGE".equals( type) ) {
                        this.Mx = mx;
                        this.Mi = mi;
                }
                else if ("CURRENT".equals(type)) {
                        this.Ix = mx;
                        this.Ii = mi;
                }
        }

        @JsonIgnore
        public Map<String, Object> getSignalDataMap() {
                Map<String, Object> env = new HashMap<>();
                if(null!=this.Mx) {
                        env.put("Mx", this.Mx);
                }
                if(null!=this.Mi) {
                        env.put("Mi", this.Mi);
                }
                if(null!=this.Ix) {
                        env.put("Ix", this.Ix);
                }
                if(null!=this.Ii) {
                        env.put("Ii", this.Ii);
                }
                return env;
        }

}
