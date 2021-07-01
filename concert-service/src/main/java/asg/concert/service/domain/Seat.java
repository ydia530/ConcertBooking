package asg.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
public class Seat {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "lable", nullable = false)
	private String label;

	private BigDecimal price;
	private boolean isBooked;

	@Column(name = "date", nullable = false)
	private LocalDateTime dateTime;

	@Version
	@Column(name = "VERSION", nullable = false)
	private long version = 1;

	public Seat() {
	}

	public Seat(String label, BigDecimal price) {
		this.label = label;
		this.price = price;
	}

	public Seat(String seatLabel, boolean b, LocalDateTime date, BigDecimal price) {
		this.label = seatLabel;
		this.isBooked = b;
		this.dateTime = date;
		this.price = price;

	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean getIsBooked(){return isBooked;}

	public void  setIsBooked(boolean state){
		this.isBooked = state;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Seat seat = (Seat) o;

		return new EqualsBuilder()
				.append(label, seat.label)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(label)
				.toHashCode();
	}

}
