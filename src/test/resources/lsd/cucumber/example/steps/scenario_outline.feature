Feature: Data table feature

  Scenario Outline: Scenario with a data table
    Given the following values for <column1> and <column2>
    When when3
    Then then3
    Examples:
      | column1  | column2  |
      | value1_1 | value1_2 |
      | value2_1 | value2_2 |
