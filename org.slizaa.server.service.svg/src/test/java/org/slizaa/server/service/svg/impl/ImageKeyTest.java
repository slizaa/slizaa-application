package org.slizaa.server.service.svg.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ImageKeyTest {

	@Test
	public void longKey_1() {
		String key = ImageKey.longKey(true, "main", "upperLeft", "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_2() {
		String key = ImageKey.longKey(true,"main", null, "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_3() {
		String key = ImageKey.longKey(true,"main", "upperLeft", null, "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_4() {
		String key = ImageKey.longKey(true,"main", "upperLeft", "upperRight", null, "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&lr=lowerRight");
	}
	
	@Test
	public void longKey_5() {
		String key = ImageKey.longKey(true,"main", "upperLeft", "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void longKey_6() {
		String key = ImageKey.longKey(true,"main", null, "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void longKey_7() {
		String key = ImageKey.longKey(true,"main", "upperLeft", null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft");
	}
	
	@Test
	public void longKey_8() {
		String key = ImageKey.longKey(true,"main", null, null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ll=lowerLeft");
	}

	@Test
	public void longKey_9() {
		String key = ImageKey.longKey(false,"main", null, null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ol=0&ll=lowerLeft");
	}

	@Test
	public void shortKey_1() {
		String key = ImageKey.shortKey("main?ll=lowerLeft");
		assertThat(key).isEqualTo("425313399.svg");
	}

	@Test
	public void shortKey_2() {
		String key = ImageKey.shortKey("main?ul=upperLeft&ll=lowerLeft");
		assertThat(key).isEqualTo("956358965.svg");
		assertThat(key).isEqualTo(ImageKey.shortKey("main?ul=upperLeft&ll=lowerLeft"));
	}

	@Test
	public void shortKey_3() {
		String key1 = ImageKey.shortKey("main?ul=upperLeft");
		assertThat(key1).isEqualTo("653000350.svg");
		String key2 = ImageKey.shortKey("main?ul=upperleft");
		assertThat(key2).isEqualTo("773054235.svg");
		assertThat(key1).isNotEqualTo(key2);
	}

	@Test(expected = NullPointerException.class)
	public void test_NotNull() {
		String key = ImageKey.longKey(true, null, null, null, "lowerLeft", null);
	}
}
