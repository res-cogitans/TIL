package hello.upload.controller;

import org.springframework.core.convert.converter.Converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataConverter implements Converter<String, ItemRestController.NestedData> {
	@Override
	public ItemRestController.NestedData convert(String source) {
		log.info("convert");
		String[] s = source.split(" ");
		return new ItemRestController.NestedData(Integer.valueOf(s[0]), s[1]);
	}
}
