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
package org.sonar.core.i18n;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RuleI18nManagerTest {

  @Test
  public void shouldGetName() {
    I18nManager i18n = mock(I18nManager.class);
    RuleI18nManager ruleI18n = new RuleI18nManager(i18n);

    ruleI18n.getName("checkstyle", "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck", Locale.ENGLISH);

    String propertyKey = "rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.name";
    verify(i18n).message(Locale.ENGLISH, propertyKey, null /* no default value */);
    verifyNoMoreInteractions(i18n);
  }

  @Test
  public void shouldGetParamDescription() {
    I18nManager i18n = mock(I18nManager.class);
    RuleI18nManager ruleI18n = new RuleI18nManager(i18n);

    ruleI18n.getParamDescription("checkstyle", "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck", "pattern", Locale.ENGLISH);

    String propertyKey = "rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.param.pattern";
    verify(i18n).message(Locale.ENGLISH, propertyKey, null /* no default value */);
    verifyNoMoreInteractions(i18n);
  }

  @Test
  public void shouldGetDescriptionFromFile() {
    String propertyKeyForName = "rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.name";

    I18nManager i18n = mock(I18nManager.class);
    when(i18n.messageFromFile(Locale.ENGLISH, "rules/checkstyle/com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.html", propertyKeyForName)).thenReturn("Description");

    RuleI18nManager ruleI18n = new RuleI18nManager(i18n);
    String description = ruleI18n.getDescription("checkstyle", "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck", Locale.ENGLISH);
    assertThat(description, is("Description"));

    verify(i18n).messageFromFile(Locale.ENGLISH, "rules/checkstyle/com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.html", propertyKeyForName);
    verifyNoMoreInteractions(i18n);
  }

  // see http://jira.codehaus.org/browse/SONAR-3319
  @Test
  public void shouldGetDescriptionFromFileWithBackwardCompatibility() {
    String propertyKeyForName = "rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.name";

    I18nManager i18n = mock(I18nManager.class);
    // this is the "old" way of storing HTML description files for rules (they are not in the "rules/<repo-key>" folder)
    when(i18n.messageFromFile(Locale.ENGLISH, "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.html", propertyKeyForName)).thenReturn("Description");

    RuleI18nManager ruleI18n = new RuleI18nManager(i18n);
    String description = ruleI18n.getDescription("checkstyle", "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck", Locale.ENGLISH);
    assertThat(description, is("Description"));

    verify(i18n).messageFromFile(Locale.ENGLISH, "rules/checkstyle/com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.html", propertyKeyForName);
    verify(i18n).messageFromFile(Locale.ENGLISH, "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.html", propertyKeyForName);
    verifyNoMoreInteractions(i18n);
  }

  @Test
  public void shoudlReturnNullIfMissingDescription() {
    I18nManager i18n = mock(I18nManager.class);
    RuleI18nManager ruleI18n = new RuleI18nManager(i18n);

    assertThat(ruleI18n.getDescription("checkstyle", "com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck", Locale.ENGLISH), nullValue());
  }

  @Test
  public void shouldBeRuleKey() {
    assertThat(RuleI18nManager.isRuleProperty("rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.name"), Is.is(true));
    assertThat(RuleI18nManager.isRuleProperty("rule.pmd.Header.name"), Is.is(true));
  }

  @Test
  public void shouldNotBeRuleKey() {
    // this is the parameter "name"
    assertThat(RuleI18nManager.isRuleProperty("rule.checkstyle.com.puppycrawl.tools.checkstyle.checks.annotation.AnnotationUseStyleCheck.param.name"), Is.is(false));
    assertThat(RuleI18nManager.isRuleProperty("by"), Is.is(false));
    assertThat(RuleI18nManager.isRuleProperty("something.else"), Is.is(false));
    assertThat(RuleI18nManager.isRuleProperty("checkstyle.page.name"), Is.is(false));
  }
}
