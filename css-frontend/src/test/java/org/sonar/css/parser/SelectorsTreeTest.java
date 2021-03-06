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
package org.sonar.css.parser;

import org.junit.Test;
import org.sonar.plugins.css.api.tree.ClassSelectorTree;
import org.sonar.plugins.css.api.tree.SelectorsTree;
import org.sonar.plugins.css.api.tree.TypeSelectorTree;

import static org.fest.assertions.Assertions.assertThat;

public class SelectorsTreeTest extends TreeTest {

  public SelectorsTreeTest() {
    super(CssLexicalGrammar.SELECTORS);
  }

  @Test
  public void selectors() {
    SelectorsTree tree;

    tree = checkParsed("h1, h2");
    assertThat(tree.selectors()).hasSize(2);
    assertThat(tree.selectors().get(0).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.selectors().get(1).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.lastSelector().compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);

    tree = checkParsed("h1 , h2");
    assertThat(tree.selectors()).hasSize(2);
    assertThat(tree.selectors().get(0).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.selectors().get(1).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.lastSelector().compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);

    tree = checkParsed("h1 , h2, .class.class");
    assertThat(tree.selectors()).hasSize(3);
    assertThat(tree.selectors().get(0).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.selectors().get(1).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(TypeSelectorTree.class);
    assertThat(tree.selectors().get(2).compoundSelectors().get(0).selectors().get(0)).isInstanceOf(ClassSelectorTree.class);
    assertThat(tree.selectors().get(2).compoundSelectors().get(0).selectors().get(1)).isInstanceOf(ClassSelectorTree.class);
    assertThat(tree.lastSelector().compoundSelectors().get(0).selectors().get(1)).isInstanceOf(ClassSelectorTree.class);
  }

  @Test
  public void notSelectors() {
    checkNotParsed(" h1, h2");
  }

  private SelectorsTree checkParsed(String toParse) {
    SelectorsTree tree = (SelectorsTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    return tree;
  }

}
