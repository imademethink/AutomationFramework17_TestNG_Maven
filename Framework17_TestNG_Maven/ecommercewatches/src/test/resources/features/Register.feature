Feature: Demo of Maven and Junit integration - Register

@register
Scenario: User registration with provided data
  When User attempts to register with following data
  | Email            | fake  |
  | Title            | Mr    |
  | NameFirst        | James |
  | NameLast         | Bond  |
  | Password         | Fake@12345 |
  | DOB_DD           | 1      |
  | DOB_MM           | 1      |
  | DOB_YYYY         | 2020   |
  | AddressNameFirst | James |
  | AddressNameLast  | Bond  |
  | Address          | MG Road PO box 007 |
  | City             | Darjiling |
  | State            | Ohio   |
  | Zip              | 33160  |
  | AdditionalInfo  | Test account |
  | Mobile           | 9876543210   |
  Then Registration should be successful



