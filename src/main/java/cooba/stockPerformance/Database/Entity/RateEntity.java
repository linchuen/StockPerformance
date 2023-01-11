package cooba.stockPerformance.Database.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@CompoundIndex(name = "compound_sc_y_m_idx", def = "{ stockcode: 1, year: -1, month: 1 }")
public class RateEntity {
    private String id;
    private int stockcode;
    private int year;
    private int month;
    private int duration;
    private String type;
    private List<Integer> intevalList;
    private List<BigDecimal> standardPointList;
    private List<BigDecimal> rateList;
}
