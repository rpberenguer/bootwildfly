package fantasymanager.data.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fantasymanager.serializer.LocalDateDeserializer;
import lombok.Data;

@Data
public class StatisticRequest {

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate endDate;
}
