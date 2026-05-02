import { useTranslation } from "react-i18next";

export const LanguageSwitcher = () => {
  const { i18n } = useTranslation();

  return (
    <div className="hidden sm:flex rounded-full p-1 bg-gray-100">
      <button 
        onClick={() => i18n.changeLanguage('en')} 
        className={`px-3 py-1.5 text-xs font-bold rounded-full transition-all ${
          i18n.language === 'en' 
            ? 'bg-white shadow text-primary-600'
            : 'text-gray-500 hover:text-gray-700'
        }`}
      >
        EN
      </button>
      <button 
        onClick={() => i18n.changeLanguage('fr')} 
        className={`px-3 py-1.5 text-xs font-bold rounded-full transition-all ${
          i18n.language === 'fr' 
            ? 'bg-white shadow text-primary-600'
            : 'text-gray-500 hover:text-gray-700'
        }`}
      >
        FR
      </button>
    </div>
  );
};
