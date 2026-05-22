const nextTranslate = require("next-translate");

module.exports = nextTranslate({
  compress: false,
  poweredByHeader: false,
  pageExtensions: ["jsx", "js"],
});
