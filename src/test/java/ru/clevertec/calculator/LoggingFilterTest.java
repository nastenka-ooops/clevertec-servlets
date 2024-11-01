package ru.clevertec.calculator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(ReplaceCamelCase.class)
public class LoggingFilterTest {

	@Mock
	ServletRequest request;

	@Mock
	ServletResponse response;

	@Mock
	FilterChain chain;

	@Spy
	LoggingFilter spyFilter;

	@BeforeEach
	public void setUp() {
		openMocks(this);
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		Enumeration<String> enumeration = Collections.enumeration(List.of("param1", "param2", "param3"));
		when(request.getParameterNames()).thenReturn(enumeration);
		when(request.getParameterValues(anyString())).thenReturn(new String[] { "value1", "value2" });

		assertDoesNotThrow(() -> spyFilter.doFilter(request, response, chain),
				"Incorrect implementation of the doFilter method");

		verify(spyFilter, times(3)).logRequestParameter(anyString(), anyString());
		verify(chain).doFilter(request, response);
	}
}