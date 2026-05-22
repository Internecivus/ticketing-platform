import React from "react";
import Application from "../app/application";
import useTranslation from "next-translate/useTranslation";

function Footer(props) {
  const { t } = useTranslation("common");

  return (
    <footer className="app-footer p-3">
      <span></span>
      <span className="text-center">
        {t("footer.copyright", { year: new Date().getFullYear(), domain: t("domain") })}
      </span>
      <span className="text-end">{t("version", Application.version)}</span>
    </footer>
  );
}

export default Footer;
