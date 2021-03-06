/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.api.server.rule;

import org.junit.Test;
import org.sonar.api.server.rule.RuleParamType;

import static org.fest.assertions.Assertions.assertThat;

public class RuleParamTypeTest {

  @Test
  public void testEquals() throws Exception {
    RuleParamType noOptions = RuleParamType.INTEGER;
    RuleParamType withOptions1 = RuleParamType.ofValues("one", "two");
    RuleParamType withOptions2 = RuleParamType.ofValues("three", "four");

    assertThat(RuleParamType.INTEGER)
      .isEqualTo(RuleParamType.INTEGER)
      .isNotEqualTo(RuleParamType.STRING)
      .isNotEqualTo("INTEGER")
      .isNotEqualTo(withOptions1)
      .isNotEqualTo(null);

    assertThat(withOptions1)
      .isEqualTo(withOptions1)
      .isNotEqualTo(noOptions)
      .isNotEqualTo(withOptions2)
      .isNotEqualTo("SINGLE_SELECT_LIST|one,two,")
      .isNotEqualTo(null);
  }

  @Test
  public void testHashCode() throws Exception {
    assertThat(RuleParamType.INTEGER.hashCode()).isEqualTo(RuleParamType.INTEGER.hashCode());
  }

  @Test
  public void testInteger() throws Exception {
    RuleParamType type = RuleParamType.INTEGER;
    assertThat(type.toString()).isEqualTo("INTEGER");
    assertThat(RuleParamType.parse(type.toString()).type()).isEqualTo("INTEGER");
    assertThat(RuleParamType.parse(type.toString()).options()).isEmpty();
    assertThat(RuleParamType.parse(type.toString()).toString()).isEqualTo("INTEGER");
  }

  @Test
  public void testListOfValues() throws Exception {
    RuleParamType selectList = RuleParamType.parse("SINGLE_SELECT_LIST|foo,bar,");
    assertThat(selectList.type()).isEqualTo("SINGLE_SELECT_LIST");
    assertThat(selectList.options()).containsOnly("foo", "bar");
    assertThat(selectList.toString()).isEqualTo("SINGLE_SELECT_LIST|foo,bar,");

    // escape values
    selectList = RuleParamType.ofValues("foo", "one,two|three,four");
    assertThat(selectList.type()).isEqualTo("SINGLE_SELECT_LIST");
    assertThat(selectList.options()).containsOnly("foo", "one,two|three,four");
    assertThat(selectList.toString()).isEqualTo("SINGLE_SELECT_LIST|foo,\"one,two|three,four\",");
  }

  @Test
  public void support_deprecated_formats() throws Exception {
    assertThat(RuleParamType.parse("b")).isEqualTo(RuleParamType.BOOLEAN);
    assertThat(RuleParamType.parse("i")).isEqualTo(RuleParamType.INTEGER);
    assertThat(RuleParamType.parse("i{}")).isEqualTo(RuleParamType.INTEGER);
    assertThat(RuleParamType.parse("s")).isEqualTo(RuleParamType.STRING);
    assertThat(RuleParamType.parse("s{}")).isEqualTo(RuleParamType.STRING);
    assertThat(RuleParamType.parse("r")).isEqualTo(RuleParamType.STRING);
    assertThat(RuleParamType.parse("TEXT")).isEqualTo(RuleParamType.TEXT);
    assertThat(RuleParamType.parse("STRING")).isEqualTo(RuleParamType.STRING);
    assertThat(RuleParamType.parse("REGULAR_EXPRESSION")).isEqualTo(RuleParamType.STRING);
    RuleParamType list = RuleParamType.parse("s[FOO,BAR]");
    assertThat(list.type()).isEqualTo("SINGLE_SELECT_LIST");
    assertThat(list.options()).containsOnly("FOO", "BAR");
  }
}
