/**
 * Created by robert on 7/26/16.
 */
public class Longitude {
    private final Double VALUE_MAX = 180.0;
    private final Double VALUE_MIN = -180.0;

    private Double m_value_degrees = null;
    private Double m_value_radians = null;

    public Longitude (Double t_value_degrees) {
        if (t_value_degrees > VALUE_MAX)
            throw new IllegalArgumentException(String.format("invalid degrees value for longitude (%1$.6f)", t_value_degrees));
        if (t_value_degrees < VALUE_MIN)
            throw new IllegalArgumentException(String.format("invalid degrees value for longitude (%1$.6f)", t_value_degrees));
        m_value_degrees = t_value_degrees;
        m_value_radians = Math.toRadians(t_value_degrees);
    }

    public Double degrees () {
        return m_value_degrees;
    }

    public Double radians () {
        return m_value_radians;
    }
}
