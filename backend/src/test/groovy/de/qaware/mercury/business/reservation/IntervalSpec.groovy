package de.qaware.mercury.business.reservation

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class IntervalSpec extends Specification {

    @Unroll
    def "Check no Overlap #i1 #condition #i2"() {
        expect:
        !i1.overlaps(i2)
        !i2.overlaps(i1)

        where:
        i1                            | condition          | i2
        Interval.of(time(1), time(2)) | 'before'           | Interval.of(time(3), time(4))
        Interval.of(time(3), time(4)) | 'after'            | Interval.of(time(1), time(2))
        Interval.of(time(1), time(2)) | 'cuddles in front' | Interval.of(time(2), time(3))
        Interval.of(time(2), time(3)) | 'cuddles in back' | Interval.of(time(1), time(2))
    }

    @Unroll
    def "Check Overlap #i1 #condition #i2"() {
        expect:
        i1.overlaps(i2)
        i2.overlaps(i1)

        where:
        i1                            | condition  | i2
        Interval.of(time(1), time(3)) | 'in front' | Interval.of(time(2), time(4))
        Interval.of(time(2), time(4)) | 'in back'  | Interval.of(time(1), time(3))
    }

    def "Check exact overlap"() {
        given:
        Interval i1 = Interval.of(time(1), time(2))
        Interval i2 = Interval.of(time(1), time(2))

        expect:
        i1.overlaps(i1)
        i1.overlaps(i2)
        i2.overlaps(i1)
        i2.overlaps(i2)
    }


    private LocalDateTime time(int num) {
        return LocalDateTime.of(2020, 1, 1, num, 0)
    }
}
