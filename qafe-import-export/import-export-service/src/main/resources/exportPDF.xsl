<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8"/>

	<xsl:param name="generateColumnHeader" select="'true'"/>
	
	<xsl:variable name="colCount"> 
		 <xsl:value-of select="count(/RESULTS/ROW/COLUMN) div count(/RESULTS/ROW)"/> 
	</xsl:variable>
	
	<xsl:template match="RESULTS">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="summarypage" page-height="11in" page-width="8.5in" margin-top="1in" margin-bottom="1in" margin-left="1in" margin-right="1in">
					<fo:region-body/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="summarypage">
				<fo:flow flow-name="xsl-region-body">
									
					<fo:block text-align="center" font-size="8pt">
						<fo:table table-layout="fixed" width="100%" border-collapse="collapse">
							<fo:table-body>
							
								<xsl:if test="$generateColumnHeader = 'true'">
									<!-- HEADER ROW -->
									<fo:table-row background-color="#000" border-left-style="solid" border-right-style="solid" border-top-style="solid">
										<xsl:for-each select="/RESULTS/ROW/COLUMN">
										<xsl:if test="$colCount = position() or $colCount &gt; position()">
											<fo:table-cell>
												<fo:block color="#fff" text-align="center" padding-top="0.5em" padding-bottom="0.5em">
													<xsl:value-of select="@ID"/>
												</fo:block>
											</fo:table-cell>
											</xsl:if>
										</xsl:for-each>
									</fo:table-row>
								</xsl:if>
								
								<!-- DATA ROW(s) -->
								<xsl:for-each select="/RESULTS/ROW[1 + count(preceding-sibling::ROW)]">
								<xsl:variable name="rowCount" select="1 + count(preceding-sibling::ROW)"/>
									<fo:table-row border-left-style="solid" border-right-style="solid" border-bottom-style="solid">
										<xsl:for-each select="/RESULTS/ROW[$rowCount]/COLUMN">
											<fo:table-cell border="solid">
												<fo:block text-align="center" padding-top="0.5em">
													<xsl:value-of select="."/>
												</fo:block>
											</fo:table-cell>
										</xsl:for-each>
									</fo:table-row>
								</xsl:for-each>
								
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
