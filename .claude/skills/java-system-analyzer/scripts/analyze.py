#!/usr/bin/env python3
"""
Java Code Analysis Script
Extracts components, relationships, APIs, and database schema from Java source code
"""

import os
import re
import json
from pathlib import Path
from typing import List, Dict, Set, Tuple

class JavaAnalyzer:
    """Analyzes Java source code to extract system design information"""
    
    def __init__(self):
        self.components = []
        self.relationships = []
        self.apis = []
        self.entities = []
        self.files_analyzed = 0
        
    def analyze_directory(self, path: str) -> Dict:
        """Analyze all Java files in a directory"""
        java_files = self._find_java_files(path)
        
        for java_file in java_files:
            self._analyze_file(java_file)
        
        return self._generate_report()
    
    def _find_java_files(self, path: str) -> List[str]:
        """Find all Java files in directory"""
        java_files = []
        for root, dirs, files in os.walk(path):
            # Skip test and build directories
            dirs[:] = [d for d in dirs if d not in ['test', 'target', 'build']]
            
            for file in files:
                if file.endswith('.java'):
                    java_files.append(os.path.join(root, file))
        
        return java_files
    
    def _analyze_file(self, filepath: str):
        """Analyze a single Java file"""
        with open(filepath, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        # Extract class information
        class_info = self._extract_class_info(content, filepath)
        if class_info:
            self.components.append(class_info)
        
        # Extract relationships
        relationships = self._extract_relationships(content)
        self.relationships.extend(relationships)
        
        # Extract APIs
        apis = self._extract_apis(content)
        self.apis.extend(apis)
        
        # Extract entity information
        if self._is_entity(content):
            entity_info = self._extract_entity_info(content)
            self.entities.append(entity_info)
        
        self.files_analyzed += 1
    
    def _extract_class_info(self, content: str, filepath: str) -> Dict:
        """Extract class definition information"""
        # Find class definition
        class_pattern = r'(?:public\s+)?(?:abstract\s+)?(?:final\s+)?class\s+(\w+)'
        match = re.search(class_pattern, content)
        
        if not match:
            return None
        
        class_name = match.group(1)
        package_name = self._extract_package(content)
        annotations = self._extract_annotations(content)
        methods = self._extract_methods(content)
        imports = self._extract_imports(content)
        
        component_type = self._classify_component(class_name, annotations, imports)
        
        return {
            'name': class_name,
            'package': package_name,
            'filepath': filepath,
            'type': component_type,
            'annotations': annotations,
            'methods': methods,
            'imports': imports
        }
    
    def _extract_package(self, content: str) -> str:
        """Extract package name from class"""
        match = re.search(r'package\s+([\w.]+);', content)
        return match.group(1) if match else 'default'
    
    def _extract_annotations(self, content: str) -> List[str]:
        """Extract class-level annotations"""
        pattern = r'@(\w+)(?:\([^)]*\))?'
        # Get annotations before class definition
        class_idx = content.find('class ')
        if class_idx != -1:
            content = content[:class_idx]
        
        return re.findall(pattern, content)
    
    def _extract_methods(self, content: str) -> List[Dict]:
        """Extract method definitions"""
        pattern = r'(?:public|private|protected)\s+(?:static\s+)?(?:synchronized\s+)?(\w+)\s+(\w+)\s*\(([^)]*)\)'
        methods = []
        
        for match in re.finditer(pattern, content):
            return_type = match.group(1)
            method_name = match.group(2)
            parameters = match.group(3)
            
            methods.append({
                'name': method_name,
                'returnType': return_type,
                'parameters': self._parse_parameters(parameters)
            })
        
        return methods
    
    def _parse_parameters(self, param_string: str) -> List[Dict]:
        """Parse method parameters"""
        if not param_string.strip():
            return []
        
        params = []
        for param in param_string.split(','):
            parts = param.strip().split()
            if len(parts) >= 2:
                params.append({
                    'type': ' '.join(parts[:-1]),
                    'name': parts[-1]
                })
        
        return params
    
    def _classify_component(self, class_name: str, annotations: List[str], imports: List[str]) -> str:
        """Classify component type based on annotations and naming"""
        annotations_lower = [a.lower() for a in annotations]
        
        if 'restcontroller' in annotations_lower or 'controller' in annotations_lower:
            return 'Controller'
        elif 'service' in annotations_lower:
            return 'Service'
        elif 'repository' in annotations_lower or 'component' in annotations_lower:
            return 'Repository'
        elif 'entity' in annotations_lower or 'document' in annotations_lower:
            return 'Entity'
        elif 'configuration' in annotations_lower:
            return 'Configuration'
        elif class_name.endswith('Controller'):
            return 'Controller'
        elif class_name.endswith('Service'):
            return 'Service'
        elif class_name.endswith('Repository'):
            return 'Repository'
        else:
            return 'Component'
    
    def _extract_relationships(self, content: str) -> List[Dict]:
        """Extract component dependencies"""
        relationships = []
        
        # Find @Autowired and constructor injection
        autowired_pattern = r'@Autowired\s+private\s+(\w+)\s+(\w+)'
        for match in re.finditer(autowired_pattern, content):
            relationships.append({
                'type': 'dependency',
                'target': match.group(1),
                'method': 'field_injection'
            })
        
        return relationships
    
    def _extract_apis(self, content: str) -> List[Dict]:
        """Extract REST API information"""
        apis = []
        
        # Find REST mappings
        mapping_pattern = r'@(GetMapping|PostMapping|PutMapping|DeleteMapping|RequestMapping)\s*\(\s*["\']?([^"\']\)]*)'\
        
        method_pattern = r'(?:public|private)\s+(\w+)\s+(\w+)\s*\(([^)]*)\)'
        
        for match in re.finditer(method_pattern, content):
            return_type = match.group(1)
            method_name = match.group(2)
            parameters = self._parse_parameters(match.group(3))
            
            apis.append({
                'method': method_name,
                'returnType': return_type,
                'parameters': parameters
            })
        
        return apis
    
    def _extract_imports(self, content: str) -> List[str]:
        """Extract import statements"""
        pattern = r'import\s+([\w.]+);'
        return re.findall(pattern, content)
    
    def _is_entity(self, content: str) -> bool:
        """Check if class is a JPA entity"""
        entity_indicators = ['@Entity', '@Table', '@Id', '@Column', '@OneToMany', '@ManyToOne']
        return any(indicator in content for indicator in entity_indicators)
    
    def _extract_entity_info(self, content: str) -> Dict:
        """Extract entity/database information"""
        class_name = self._extract_class_name(content)
        table_name = self._extract_table_name(content)
        fields = self._extract_fields(content)
        relationships = self._extract_entity_relationships(content)
        
        return {
            'class': class_name,
            'table': table_name,
            'fields': fields,
            'relationships': relationships
        }
    
    def _extract_class_name(self, content: str) -> str:
        """Extract class name"""
        match = re.search(r'class\s+(\w+)', content)
        return match.group(1) if match else 'Unknown'
    
    def _extract_table_name(self, content: str) -> str:
        """Extract table name from @Table annotation"""
        match = re.search(r'@Table\s*\(\s*name\s*=\s*["\']([^"\']+)["\']', content)
        if match:
            return match.group(1)
        
        # Fallback to class name
        return self._extract_class_name(content).lower()
    
    def _extract_fields(self, content: str) -> List[Dict]:
        """Extract entity fields"""
        fields = []
        
        # Find @Column annotations with field definitions
        pattern = r'@Column\s*\([^)]*\)\s+(?:private\s+)?(\w+)\s+(\w+)'
        
        for match in re.finditer(pattern, content):
            field_type = match.group(1)
            field_name = match.group(2)
            
            fields.append({
                'name': field_name,
                'type': field_type
            })
        
        return fields
    
    def _extract_entity_relationships(self, content: str) -> List[Dict]:
        """Extract JPA entity relationships"""
        relationships = []
        
        rel_pattern = r'@(OneToMany|ManyToOne|ManyToMany|OneToOne)\s*(?:\([^)]*\))?\s+(?:private\s+)?(\w+)\s+(\w+)'
        
        for match in re.finditer(rel_pattern, content):
            rel_type = match.group(1)
            target_type = match.group(2)
            field_name = match.group(3)
            
            relationships.append({
                'type': rel_type,
                'targetEntity': target_type,
                'fieldName': field_name
            })
        
        return relationships
    
    def _generate_report(self) -> Dict:
        """Generate analysis report"""
        return {
            'summary': {
                'filesAnalyzed': self.files_analyzed,
                'componentsFound': len(self.components),
                'apisFound': len(self.apis),
                'entitiesFound': len(self.entities),
                'relationshipsFound': len(self.relationships)
            },
            'components': self.components,
            'relationships': self.relationships,
            'apis': self.apis,
            'entities': self.entities
        }


def main():
    """Main execution"""
    import sys
    
    if len(sys.argv) < 2:
        print("Usage: python analyzer.py <path_to_java_project>")
        sys.exit(1)
    
    path = sys.argv[1]
    analyzer = JavaAnalyzer()
    
    print(f"Analyzing Java project at: {path}")
    report = analyzer.analyze_directory(path)
    
    print(json.dumps(report, indent=2))


if __name__ == '__main__':
    main()
