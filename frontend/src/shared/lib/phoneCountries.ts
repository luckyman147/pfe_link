import { getCountries, getCountryCallingCode } from 'libphonenumber-js/min';

const displayNames = new Intl.DisplayNames(['en'], { type: 'region' });

const flagFromCode = (countryCode: string): string =>
  countryCode
    .toUpperCase()
    .replace(/./g, (char) => String.fromCodePoint(127397 + char.charCodeAt(0)));

export interface PhoneCountry {
  code: string;
  name: string;
  dialCode: string;
  flag: string;
}

export const phoneCountries: PhoneCountry[] = getCountries()
  .map((code) => ({
    code,
    name: displayNames.of(code) || code,
    dialCode: `+${getCountryCallingCode(code)}`,
    flag: flagFromCode(code),
  }))
  .sort((a, b) => a.name.localeCompare(b.name));
