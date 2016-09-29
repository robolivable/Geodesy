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

    public Longitude offset(Latitude t_latitude, EllipsoidDatum.Bearing t_bearing, Double t_meters) {
        Double t_b_value_radians;
        switch (t_bearing) {
            case EAST:
                t_b_value_radians = m_value_radians + (t_meters/(EllipsoidDatum.EARTH.a()*Math.cos(t_latitude.radians())));
                return new Longitude(Math.toDegrees(t_b_value_radians));
            case WEST:
                t_b_value_radians = m_value_radians - (t_meters/(EllipsoidDatum.EARTH.a()*Math.cos(t_latitude.radians())));
                return new Longitude(Math.toDegrees(t_b_value_radians));
            default:
                throw new IllegalArgumentException("invalid bearing for longitude offset");
        }
    }

    public String toString() {
        return String.format("%s", m_value_degrees);
    }
}
