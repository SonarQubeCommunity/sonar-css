/*
 * SonarQube CSS Plugin
 * Copyright (C) 2013-2016 Tamas Kende and David RACODON
 * mailto: kende.tamas@gmail.com and david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css.model.property.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.sonar.css.model.property.validator.valueelement.DelimiterValidator;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.ValueTree;

/**
 * Validator to check property values than can be multiplied (comma-separated list): [xxx]#
 * See https://developer.mozilla.org/en-US/docs/Web/CSS/Value_definition_syntax#Hash_mark_()
 */
public class HashMultiplierValidator implements ValueValidator {

  private final List<ValueElementValidator> validators;
  private static final ValueElementValidator COMMA_VALIDATOR = new DelimiterValidator(",");

  public HashMultiplierValidator(ValueElementValidator... validators) {
    this.validators = Arrays.asList(validators);
  }

  @Override
  public boolean isValid(ValueTree valueTree) {
    boolean isValid;
    boolean isCommaSeparatorExpected = false;
    for (Tree valueElement : valueTree.sanitizedValueElements()) {
      isValid = false;
      if (isCommaSeparatorExpected) {
        if (!COMMA_VALIDATOR.isValid(valueElement)) {
          return false;
        }
        isCommaSeparatorExpected = false;
      } else {
        for (ValueElementValidator validator : validators) {
          if (validator.isValid(valueElement)) {
            isValid = true;
            break;
          }
        }
        if (!isValid) {
          return false;
        }
        isCommaSeparatorExpected = true;
      }
    }
    return true;
  }

  @Override
  public String getValidatorFormat() {
    StringBuilder format = new StringBuilder();

    String joinedValidatorsFormat = validators.stream()
      .map(Validator::getValidatorFormat)
      .collect(Collectors.joining(" | "));

    if (joinedValidatorsFormat.contains(" | ")) {
      format.append("[ ");
    }
    format.append(joinedValidatorsFormat);
    if (joinedValidatorsFormat.contains(" | ")) {
      format.append(" ]");
    }
    format.append("#");
    return format.toString();
  }

}
