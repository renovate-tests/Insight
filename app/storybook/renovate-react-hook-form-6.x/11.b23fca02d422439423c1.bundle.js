(window.webpackJsonp=window.webpackJsonp||[]).push([[11],{2301:function(module,__webpack_exports__,__webpack_require__){"use strict";__webpack_require__.r(__webpack_exports__),__webpack_require__.d(__webpack_exports__,"default",(function(){return tables_CountByLocationDataTable_CountByLocationDataTable}));__webpack_require__(48);var react=__webpack_require__(0),react_default=__webpack_require__.n(react),column_categorical=__webpack_require__(601),column_numerical=__webpack_require__(2276),stateful_data_table=__webpack_require__(2282),block=__webpack_require__(43),COLUMNS=[Object(column_categorical.a)({title:"Continent name",mapDataToValue:function mapDataToValue(data){return data["location.continentName"]}}),Object(column_categorical.a)({title:"Country name",mapDataToValue:function mapDataToValue(data){return data["location.countryName"]}}),Object(column_numerical.a)({title:"Count",mapDataToValue:function mapDataToValue(data){return data.count}})],CountByLocationDataTable_CountByLocationDataTable=function CountByLocationDataTable(_ref){var countByLocation=_ref.data,height=_ref.height,rows=Object(react.useMemo)((function(){return countByLocation.map((function(row){return{data:row}}))}),[countByLocation]);return react_default.a.createElement(block.a,{height:height},react_default.a.createElement(stateful_data_table.a,{columns:COLUMNS,rows:rows,$style:{width:"100%",overflow:"auto"},rowHeight:50}))};CountByLocationDataTable_CountByLocationDataTable.displayName="CountByLocationDataTable";var tables_CountByLocationDataTable_CountByLocationDataTable=CountByLocationDataTable_CountByLocationDataTable;try{CountByLocationDataTable_CountByLocationDataTable.displayName="CountByLocationDataTable",CountByLocationDataTable_CountByLocationDataTable.__docgenInfo={description:"",displayName:"CountByLocationDataTable",props:{data:{defaultValue:null,description:"",name:"data",required:!0,type:{name:"CountByLocation"}},height:{defaultValue:null,description:"",name:"height",required:!0,type:{name:"string"}}}},"undefined"!=typeof STORYBOOK_REACT_CLASSES&&(STORYBOOK_REACT_CLASSES["src/modules/insights/components/tables/CountByLocationDataTable/CountByLocationDataTable.tsx#CountByLocationDataTable"]={docgenInfo:CountByLocationDataTable_CountByLocationDataTable.__docgenInfo,name:"CountByLocationDataTable",path:"src/modules/insights/components/tables/CountByLocationDataTable/CountByLocationDataTable.tsx#CountByLocationDataTable"})}catch(__react_docgen_typescript_loader_error){}}}]);
//# sourceMappingURL=11.b23fca02d422439423c1.bundle.js.map