package ee.danych.nutrimatch.components;

import ee.danych.nutrimatch.exceptions.XlsxFileNotFoundException;
import ee.danych.nutrimatch.model.entity.Element;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.entity.ProductName;
import ee.danych.nutrimatch.model.enums.Language;
import ee.danych.nutrimatch.repository.ExceptionChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Lazy
@Component
public class ProductFileSerializer {

    @Value("${product-serializer.path}")
    private String productFilePath;

    public List<Product> getProductsFromExcel() throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        Workbook workbook;
        FileInputStream file;
        try {
            file = getFileInputStream(productFilePath);
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new XlsxFileNotFoundException(productFilePath);
        }

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) rowIterator.next();
        List<Product> products = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Product product = new Product();

            Iterator<Cell> cellIterator = row.cellIterator();

            Element element;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                ExceptionChecker exceptionCheckerNumericCell = cell::getNumericCellValue;
                ExceptionChecker exceptionCheckerStringCell = cell::getStringCellValue;
                if (exceptionCheckerStringCell.isExceptionSafe()) {
                    if (cell.getStringCellValue().trim().isEmpty() || cell.getStringCellValue().equals("-")) {
                        continue;
                    }
                }
                if (exceptionCheckerNumericCell.isExceptionSafe()) {
                    if (cell.getNumericCellValue() == 0) {
                        continue;
                    }
                }
                switch (cell.getColumnIndex()) {
                    case 0:
                        try {
                            product.setCode(String.valueOf((int) cell.getNumericCellValue()));
                        } catch (IllegalStateException illegalArgumentException) {
                            product.setCode(cell.getStringCellValue());
                            break;
                        }

                        break;
                    case 1:
                        ProductName productNameEt = new ProductName();
                        productNameEt.setProduct(product);
                        productNameEt.setLanguage(Language.ET);
                        productNameEt.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameEt);
                        break;
                    case 3:
                        ProductName productNameRu = new ProductName();
                        productNameRu.setProduct(product);
                        productNameRu.setLanguage(Language.RU);
                        productNameRu.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameRu);
                        break;
                    case 2:
                        ProductName productNameEn = new ProductName();
                        productNameEn.setProduct(product);
                        productNameEn.setLanguage(Language.EN);
                        productNameEn.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameEn);
                        break;
                    case 4:
                        ProductName productNameLv = new ProductName();
                        productNameLv.setProduct(product);
                        productNameLv.setLanguage(Language.LV);
                        productNameLv.setName(cell.getStringCellValue());
                        product.getProductNames().add(productNameLv);
                        break;
                    case 5:
                        product.setSynonyms(cell.getStringCellValue());
                        break;
                    case 6:
                        product.setFoodGroup(cell.getStringCellValue());
                        break;
                    case 9:
                        product.setEnergy(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        break;
                    case 10:
                        element = new Element();
                        element.setProduct(product);
                        element.setName("Carbohydrates");
                        element.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(element);
                        break;
                    case 11:
                        Element fats = new Element();
                        fats.setProduct(product);
                        fats.setName("Fats");
                        fats.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fats);
                        break;
                    case 12:
                        Element fibers = new Element();
                        fibers.setProduct(product);
                        fibers.setName("Dietary fibre");
                        fibers.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fibers);
                        break;
                    case 13:
                        Element proteins = new Element();
                        proteins.setProduct(product);
                        proteins.setName("Proteins");
                        proteins.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(proteins);
                        break;
                    case 14:
                        Element alcohol = new Element();
                        alcohol.setProduct(product);
                        alcohol.setName("Alcohol");
                        alcohol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(alcohol);
                        break;
                    case 15:
                        Element water = new Element();
                        water.setProduct(product);
                        water.setName("Water");
                        water.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(water);
                        break;
                    case 16:
                        Element ash = new Element();
                        ash.setProduct(product);
                        ash.setName("Ash");
                        ash.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(ash);
                        break;
                    case 17:
                        Element availableCarbs = new Element();
                        availableCarbs.setProduct(product);
                        availableCarbs.setName("Available Carbohydrates");
                        availableCarbs.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(availableCarbs);
                        break;
                    case 18:
                        Element starch = new Element();
                        starch.setProduct(product);
                        starch.setName("Starch");
                        starch.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(starch);
                        break;
                    case 19:
                        Element polyols = new Element();
                        polyols.setProduct(product);
                        polyols.setName("Polyols");
                        polyols.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(polyols);
                        break;
                    case 20:
                        Element sorbitol = new Element();
                        sorbitol.setProduct(product);
                        sorbitol.setName("Sorbitol");
                        sorbitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sorbitol);
                        break;
                    case 21:
                        Element mannitol = new Element();
                        mannitol.setProduct(product);
                        mannitol.setName("Mannitol");
                        mannitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(mannitol);
                        break;
                    case 22:
                        Element isomalt = new Element();
                        isomalt.setProduct(product);
                        isomalt.setName("Isomalt");
                        isomalt.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(isomalt);
                        break;
                    case 23:
                        Element maltitol = new Element();
                        maltitol.setProduct(product);
                        maltitol.setName("Maltitol");
                        maltitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(maltitol);
                        break;
                    case 24:
                        Element lactitol = new Element();
                        lactitol.setProduct(product);
                        lactitol.setName("Lactitol");
                        lactitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(lactitol);
                        break;
                    case 25:
                        Element xylitol = new Element();
                        xylitol.setProduct(product);
                        xylitol.setName("Xylitol");
                        xylitol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(xylitol);
                        break;
                    case 26:
                        Element erythritol = new Element();
                        erythritol.setProduct(product);
                        erythritol.setName("Erythritol");
                        erythritol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(erythritol);
                        break;
                    case 27:
                        Element sugars = new Element();
                        sugars.setProduct(product);
                        sugars.setName("Sugars");
                        sugars.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sugars);
                        break;
                    case 28:
                        Element sucrose = new Element();
                        sucrose.setProduct(product);
                        sucrose.setName("Sucrose");
                        sucrose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sucrose);
                        break;
                    case 29:
                        Element lactose = new Element();
                        lactose.setProduct(product);
                        lactose.setName("Lactose");
                        lactose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(lactose);
                        break;
                    case 30:
                        Element maltose = new Element();
                        maltose.setProduct(product);
                        maltose.setName("Maltose");
                        maltose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(maltose);
                        break;
                    case 31:
                        Element glucose = new Element();
                        glucose.setProduct(product);
                        glucose.setName("Glucose");
                        glucose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(glucose);
                        break;
                    case 32:
                        Element fructose = new Element();
                        fructose.setProduct(product);
                        fructose.setName("Fructose");
                        fructose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fructose);
                        break;
                    case 33:
                        Element galactose = new Element();
                        galactose.setProduct(product);
                        galactose.setName("Galactose");
                        galactose.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(galactose);
                        break;
                    case 34:
                        Element fattyAcids = new Element();
                        fattyAcids.setProduct(product);
                        fattyAcids.setName("Fatty acids");
                        fattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(fattyAcids);
                        break;
                    case 35:
                        Element saturatedFattyAcids = new Element();
                        saturatedFattyAcids.setProduct(product);
                        saturatedFattyAcids.setName("Saturated fatty acids");
                        saturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(saturatedFattyAcids);
                        break;
                    case 36:
                        Element monounsaturatedFattyAcids = new Element();
                        monounsaturatedFattyAcids.setProduct(product);
                        monounsaturatedFattyAcids.setName("Monounsaturated fatty acids");
                        monounsaturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(monounsaturatedFattyAcids);
                        break;
                    case 37:
                        Element polyunsaturatedFattyAcids = new Element();
                        polyunsaturatedFattyAcids.setProduct(product);
                        polyunsaturatedFattyAcids.setName("Polyunsaturated fatty acids");
                        polyunsaturatedFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(polyunsaturatedFattyAcids);
                        break;
                    case 38:
                        Element transFattyAcids = new Element();
                        transFattyAcids.setProduct(product);
                        transFattyAcids.setName("Trans fatty acids");
                        transFattyAcids.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(transFattyAcids);
                        break;
                    case 39:
                        Element palmiticAcid = new Element();
                        palmiticAcid.setProduct(product);
                        palmiticAcid.setName("Palmitic acid (C16)");
                        palmiticAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(palmiticAcid);
                        break;
                    case 40:
                        Element stearicAcid = new Element();
                        stearicAcid.setProduct(product);
                        stearicAcid.setName("Stearic acid (C18)");
                        stearicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(stearicAcid);
                        break;
                    case 41:
                        Element linoleicAcid = new Element();
                        linoleicAcid.setProduct(product);
                        linoleicAcid.setName("Linoleic acid (C18:2)");
                        linoleicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(linoleicAcid);
                        break;
                    case 42:
                        Element linolenicAcid = new Element();
                        linolenicAcid.setProduct(product);
                        linolenicAcid.setName("Linolenic acid (C18:3)");
                        linolenicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(linolenicAcid);
                        break;
                    case 43:
                        Element cholesterol = new Element();
                        cholesterol.setProduct(product);
                        cholesterol.setName("Cholesterol");
                        cholesterol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(cholesterol);
                        break;
                    case 44:
                        Element sodium = new Element();
                        sodium.setProduct(product);
                        sodium.setName("Sodium");
                        sodium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(sodium);
                        break;
                    case 45:
                        Element potassium = new Element();
                        potassium.setProduct(product);
                        potassium.setName("Potassium");
                        potassium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(potassium);
                        break;
                    case 46:
                        Element calcium = new Element();
                        calcium.setProduct(product);
                        calcium.setName("Calcium");
                        calcium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(calcium);
                        break;
                    case 47:
                        Element magnesium = new Element();
                        magnesium.setProduct(product);
                        magnesium.setName("Magnesium");
                        magnesium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(magnesium);
                        break;
                    case 48:
                        Element phosphorus = new Element();
                        phosphorus.setProduct(product);
                        phosphorus.setName("Phosphorus");
                        phosphorus.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(phosphorus);
                        break;
                    case 49:
                        Element iron = new Element();
                        iron.setProduct(product);
                        iron.setName("Iron");
                        iron.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(iron);
                        break;
                    case 50:
                        Element zinc = new Element();
                        zinc.setProduct(product);
                        zinc.setName("Zinc");
                        zinc.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(zinc);
                        break;
                    case 51:
                        Element copper = new Element();
                        copper.setProduct(product);
                        copper.setName("Copper");
                        copper.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(copper);
                        break;
                    case 52:
                        Element manganese = new Element();
                        manganese.setProduct(product);
                        manganese.setName("Manganese");
                        manganese.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(manganese);
                        break;
                    case 53:
                        Element iodine = new Element();
                        iodine.setProduct(product);
                        iodine.setName("Iodine");
                        iodine.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(iodine);
                        break;
                    case 54:
                        Element selenium = new Element();
                        selenium.setProduct(product);
                        selenium.setName("Selenium");
                        selenium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(selenium);
                        break;
                    case 55:
                        Element chromium = new Element();
                        chromium.setProduct(product);
                        chromium.setName("Chromium");
                        chromium.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(chromium);
                        break;
                    case 56:
                        Element nickel = new Element();
                        nickel.setProduct(product);
                        nickel.setName("Nickel");
                        nickel.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(nickel);
                        break;
                    case 57:
                        Element vitaminA = new Element();
                        vitaminA.setProduct(product);
                        vitaminA.setName("Vitamin A");
                        vitaminA.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminA);
                        break;
                    case 58:
                        Element retinol = new Element();
                        retinol.setProduct(product);
                        retinol.setName("Retinol");
                        retinol.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(retinol);
                        break;
                    case 59:
                        Element betaCarotene = new Element();
                        betaCarotene.setProduct(product);
                        betaCarotene.setName("Beta-carotene");
                        betaCarotene.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(betaCarotene);
                        break;
                    case 60:
                        Element vitaminD = new Element();
                        vitaminD.setProduct(product);
                        vitaminD.setName("Vitamin D");
                        vitaminD.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminD);
                        break;
                    case 61:
                        Element vitaminD3 = new Element();
                        vitaminD3.setProduct(product);
                        vitaminD3.setName("Vitamin D3");
                        vitaminD3.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminD3);
                        break;
                    case 62:
                        Element vitaminE = new Element();
                        vitaminE.setProduct(product);
                        vitaminE.setName("Vitamin E");
                        vitaminE.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminE);
                        break;
                    case 63:
                        Element vitaminK = new Element();
                        vitaminK.setProduct(product);
                        vitaminK.setName("Vitamin K");
                        vitaminK.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminK);
                        break;
                    case 64:
                        Element vitaminB1 = new Element();
                        vitaminB1.setProduct(product);
                        vitaminB1.setName("Vitamin B1");
                        vitaminB1.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB1);
                        break;
                    case 65:
                        Element vitaminB2 = new Element();
                        vitaminB2.setProduct(product);
                        vitaminB2.setName("Vitamin B2");
                        vitaminB2.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB2);
                        break;
                    case 66:
                        Element niacinEquivalents = new Element();
                        niacinEquivalents.setProduct(product);
                        niacinEquivalents.setName("Niacin equivalents, total");
                        niacinEquivalents.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacinEquivalents);
                        break;

                    case 67:
                        Element niacin = new Element();
                        niacin.setProduct(product);
                        niacin.setName("Niacin");
                        niacin.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacin);
                        break;

                    case 68:
                        Element niacinFromTryptophan = new Element();
                        niacinFromTryptophan.setProduct(product);
                        niacinFromTryptophan.setName("Niacin from Tryptophan");
                        niacinFromTryptophan.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(niacinFromTryptophan);
                        break;
                    case 69:
                        Element pathonicAcid = new Element();
                        pathonicAcid.setProduct(product);
                        pathonicAcid.setName("Pantothenic acid");
                        pathonicAcid.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(pathonicAcid);
                        break;
                    case 70:
                        Element vitaminB6 = new Element();
                        vitaminB6.setProduct(product);
                        vitaminB6.setName("Vitamin B6");
                        vitaminB6.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB6);
                        break;
                    case 71:
                        Element biotin = new Element();
                        biotin.setProduct(product);
                        biotin.setName("Biotin");
                        biotin.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(biotin);
                        break;
                    case 73:
                        Element vitaminB12 = new Element();
                        vitaminB12.setProduct(product);
                        vitaminB12.setName("Vitamin B12");
                        vitaminB12.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminB12);
                        break;
                    case 72:
                        Element folates = new Element();
                        folates.setProduct(product);
                        folates.setName("Folate");
                        folates.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(folates);
                        break;
                    case 74:
                        Element vitaminC = new Element();
                        vitaminC.setProduct(product);
                        vitaminC.setName("Vitamin C");
                        vitaminC.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(vitaminC);
                        break;
                    case 75:
                        Element saltEquivalent = new Element();
                        saltEquivalent.setProduct(product);
                        saltEquivalent.setName("Salt equivalent");
                        saltEquivalent.setQuantity(BigDecimal.valueOf(cell.getNumericCellValue()).setScale(4, RoundingMode.HALF_UP));
                        product.getElements().add(saltEquivalent);
                        break;
                }
            }
            products.add(product);
        }

        workbook.close();
        file.close();
        return products;
    }

    private FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(path);
    }

}
