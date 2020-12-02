/* required methods */
var gulp = require("gulp");
var concat = require("gulp-concat"); /*CSS ve JS dosyalarınızı tek bir dosyada toplayabilirsiniz */
var rename = require("gulp-rename"); /*isim değiştirme */
var uglify = require("gulp-uglify"); /* Javascript dosyalarınızı  */
var runSequence = require("run-sequence"); /* Belirtilen sırayla gulp methodları çalıştırır */
var watch = require("gulp-watch"); /* Değişiklikleri takip eder otomatik değiştirir */
var less = require("gulp-less"); /*Less  */
var autoprefixer = require("gulp-autoprefixer"); /*CSS kodlarına eski browserlara göre  -webkit gibi eklentiler ekler */
var sourcemaps = require("gulp-sourcemaps"); /*Birleştirilmiş sass kodlarının hangi sass da olduğunu bulur */
/*var concatCss = require("gulp-concat-css");
var cleanCSS = require("gulp-clean-css");*/

gulp.task("scripts", function() {
  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/jquery/js/jquery-2.1.0.min.js"
    ])
    .pipe(concat("jquery.js"))
    .pipe(rename("jquery.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/bootstrap/js/bootstrap.min.js"
    ])
    .pipe(concat("bootstrap.js"))
    .pipe(rename("bootstrap.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/apex-charts/js/apexcharts.min.js"
    ])
    .pipe(concat("apexcharts.js"))
    .pipe(rename("apexcharts.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src(["./src/main/webapp/resources/core/3rdParty/c3/js/c3.min.js"])
    .pipe(concat("c3.js"))
    .pipe(rename("c3.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/tooltipster/js/tooltipster.bundle.min.js"
    ])
    .pipe(concat("tooltipster.js"))
    .pipe(rename("tooltipster.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src(["./src/main/webapp/resources/core/javascript/theme/chart.min.js"])
    .pipe(concat("chart.js"))
    .pipe(rename("chart.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src([
      "./src/main/webapp/resources/core/javascript/theme/admin-lte.min.js",
      "./src/main/webapp/resources/core/javascript/theme/admintemplate.js",
      "./src/main/webapp/resources/core/javascript/theme/slimscroll.min.js",
      "./src/main/webapp/resources/core/javascript/theme/slideout.min.js",
      "./src/main/webapp/resources/core/javascript/theme/adminslide.js"
    ])
    .pipe(concat("main.js"))
    .pipe(rename("main.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));

  gulp
    .src(["./src/main/webapp/resources/core/javascript/custom/custom.js"])
    .pipe(concat("custom.js"))
    .pipe(rename("custom.min.js"))
    .pipe(uglify())
    .pipe(gulp.dest("./src/main/webapp/resources/core/js"));
});

gulp.task("less", function() {
  gulp
    .src(["./src/main/webapp/resources/core/less/primefaces-admin/theme.less"])
    .pipe(sourcemaps.init())
    .pipe(less({ compress: true }))
    .pipe(autoprefixer("last 15 version"))
    .pipe(concat("style.css"))
    .pipe(rename("style.min.css"))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest("./src/main/webapp/resources/core/css"));

  gulp
    .src(["./src/main/webapp/resources/core/less/egem/admin.less"])
    .pipe(sourcemaps.init())
    .pipe(less({ compress: true }))
    .pipe(autoprefixer("last 15 version"))
    .pipe(concat("admin.css"))
    .pipe(rename("admin.min.css"))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest("./src/main/webapp/resources/core/css"));

  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster.bundle.min.css",
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster-sideTip-borderless.min.css",
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster-sideTip-light.min.css",
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster-sideTip-noir.min.css",
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster-sideTip-punk.min.css",
      "./src/main/webapp/resources/core/3rdParty/tooltipster/css/tooltipster-sideTip-shadow.min.css"
    ])
    .pipe(sourcemaps.init())
    .pipe(less({ compress: true }))
    .pipe(autoprefixer("last 15 version"))
    .pipe(concat("tooltipster.css"))
    .pipe(rename("tooltipster.min.css"))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest("./src/main/webapp/resources/core/css"));

  gulp
    .src([
      "./src/main/webapp/resources/core/3rdParty/apex-charts/css/apexcharts.css"
    ])
    .pipe(sourcemaps.init())
    .pipe(less({ compress: true }))
    .pipe(autoprefixer("last 15 version"))
    .pipe(concat("apexcharts.css"))
    .pipe(rename("apexcharts.min.css"))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest("./src/main/webapp/resources/core/css"));

  gulp
    .src(["./src/main/webapp/resources/core/3rdParty/c3/css/c3.css"])
    .pipe(sourcemaps.init())
    .pipe(less({ compress: true }))
    .pipe(autoprefixer("last 15 version"))
    .pipe(concat("c3.css"))
    .pipe(rename("c3.min.css"))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest("./src/main/webapp/resources/core/css"));
});

gulp.task("watch", function() {
  gulp.watch("./src/main/webapp/resources/core/javascripts/**/**/*.js", [
    "scripts"
  ]);
  gulp.watch("./src/main/webapp/resources/core/less/**/**/*.less", ["less"]);
});

gulp.task("default", function(callback) {
  runSequence("scripts", "less", "watch", callback);
});
