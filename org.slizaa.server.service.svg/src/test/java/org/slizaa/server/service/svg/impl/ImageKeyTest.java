package org.slizaa.server.service.svg.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ImageKeyTest {

	@Test
	public void test1() {
		String key = ImageKey.key("main", "upperLeft", "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void test2() {
		String key = ImageKey.key("main", null, "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void test3() {
		String key = ImageKey.key("main", "upperLeft", null, "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void test4() {
		String key = ImageKey.key("main", "upperLeft", "upperRight", null, "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&lr=lowerRight");
	}
	
	@Test
	public void test5() {
		String key = ImageKey.key("main", "upperLeft", "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void test6() {
		String key = ImageKey.key("main", null, "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void test7() {
		String key = ImageKey.key("main", "upperLeft", null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft");
	}
	
	@Test
	public void test8() {
		String key = ImageKey.key("main", null, null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ll=lowerLeft");
	}
}
