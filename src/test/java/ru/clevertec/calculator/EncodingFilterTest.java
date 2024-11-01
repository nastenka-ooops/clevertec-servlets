package ru.clevertec.calculator;

import jakarta.servlet.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(ReplaceCamelCase.class)
public class EncodingFilterTest {

	@Mock
	ServletRequest request;

	@Mock
	ServletResponse response;

	@Mock
	FilterChain chain;

	@Mock
	FilterConfig config;

	EncodingFilter filter;

	@BeforeEach
	public void setUp() throws ServletException {
		openMocks(this);
		filter = new EncodingFilter();
	}

	@ParameterizedTest
	@CsvSource({ "'UTF-8'", "'ISO-8859-1'" })
	public void testDoFilter(String encoding) throws ServletException, IOException {
		when(config.getInitParameter("encoding")).thenReturn(encoding);
		filter.init(config);

		assertDoesNotThrow(() -> filter.doFilter(request, response, chain),
				"Incorrect implementation of the doFilter method");

		verify(request).setCharacterEncoding(encoding);
		verify(response).setCharacterEncoding(encoding);
		verify(chain).doFilter(request, response);
	}
}