/**
 * Created by robert on 7/26/16.
 */
public enum EllipsoidDatum {
    EARTH(6378137.0d,  6356752.314140d); // Geodetic Reference System 1980 (GDA94)

    private final double m_semi_major_axis;
    private final double m_semi_minor_axis;
    private final double m_flattening;
    private final double m_inverse_flattening;

    EllipsoidDatum (double t_a, double t_b) {
        m_semi_major_axis = t_a;
        m_semi_minor_axis = t_b;
        m_flattening = (m_semi_major_axis - m_semi_minor_axis) / m_semi_major_axis;
        m_inverse_flattening = 1 / m_flattening;
    }

    double a() {
        return m_semi_major_axis;
    }

    double b() {
        return m_semi_minor_axis;
    }

    double f() {
        return m_flattening;
    }

    double _f() {
        return m_inverse_flattening;
    }
}
